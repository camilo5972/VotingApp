package com.example.votingapp;

import static org.web3j.crypto.Hash.sha3String;
import static org.web3j.utils.Numeric.cleanHexPrefix;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.3.
 */
@SuppressWarnings("rawtypes")
public class Voting extends Contract {
    public static final String BINARY = "60806040525f805460ff60a01b19169055348015601a575f80fd5b505f80546001600160a01b03191633179055610a61806100395f395ff3fe608060405234801561000f575f80fd5b5060043610610090575f3560e01c80632479ecb8116100635780632479ecb81461010d5780632d35a8a2146101205780633477ee2e14610137578063c631b29214610159578063f851a44014610161575f80fd5b80630121b93f1461009457806306a49fce146100a957806309eef43e146100c85780631750a3d0146100fa575b5f80fd5b6100a76100a2366004610621565b61018b565b005b6100b161029e565b6040516100bf929190610666565b60405180910390f35b6100ea6100d63660046106fc565b60026020525f908152604090205460ff1681565b60405190151581526020016100bf565b6100a761010836600461073d565b610431565b5f546100ea90600160a01b900460ff1681565b61012960035481565b6040519081526020016100bf565b61014a610145366004610621565b61053d565b6040516100bf939291906107f2565b6100a76105e4565b5f54610173906001600160a01b031681565b6040516001600160a01b0390911681526020016100bf565b5f54600160a01b900460ff16156101bd5760405162461bcd60e51b81526004016101b49061081a565b60405180910390fd5b335f9081526002602052604090205460ff161561020c5760405162461bcd60e51b815260206004820152600d60248201526c59612068617320766f7461646f60981b60448201526064016101b4565b5f8181526001602052604081205490036102615760405162461bcd60e51b8152602060048201526016602482015275456c2063616e64696461746f206e6f2065786973746560501b60448201526064016101b4565b5f81815260016020526040812060020180549161027d8361085b565b9091555050335f908152600260205260409020805460ff1916600117905550565b6060805f60035467ffffffffffffffff8111156102bd576102bd610729565b6040519080825280602002602001820160405280156102e6578160200160208202803683370190505b5090505f60035467ffffffffffffffff81111561030557610305610729565b60405190808252806020026020018201604052801561033857816020015b60608152602001906001900390816103235790505b5090505f5b600354811015610427575f8181526001602052604090208054845185908490811061036a5761036a61087f565b60200260200101818152505080600101805461038590610893565b80601f01602080910402602001604051908101604052809291908181526020018280546103b190610893565b80156103fc5780601f106103d3576101008083540402835291602001916103fc565b820191905f5260205f20905b8154815290600101906020018083116103df57829003601f168201915b50505050508383815181106104135761041361087f565b60209081029190910101525060010161033d565b5090939092509050565b5f546001600160a01b0316331461045a5760405162461bcd60e51b81526004016101b4906108cb565b5f54600160a01b900460ff16156104835760405162461bcd60e51b81526004016101b49061081a565b5f82815260016020526040902054156104d75760405162461bcd60e51b8152602060048201526016602482015275456c2063616e64696461746f2079612065786973746560501b60448201526064016101b4565b6040805160608101825283815260208082018481525f8385018190528681526001928390529390932082518155925191929190820190610517908261096b565b506040919091015160029091015560038054905f6105348361085b565b91905055505050565b600160208190525f91825260409091208054918101805461055d90610893565b80601f016020809104026020016040519081016040528092919081815260200182805461058990610893565b80156105d45780601f106105ab576101008083540402835291602001916105d4565b820191905f5260205f20905b8154815290600101906020018083116105b757829003601f168201915b5050505050908060020154905083565b5f546001600160a01b0316331461060d5760405162461bcd60e51b81526004016101b4906108cb565b5f805460ff60a01b1916600160a01b179055565b5f60208284031215610631575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b604080825283519082018190525f906020906060840190828701845b8281101561069e57815184529284019290840190600101610682565b50505083810382850152845180825282820190600581901b830184018785015f5b838110156106ed57601f198684030185526106db838351610638565b948701949250908601906001016106bf565b50909998505050505050505050565b5f6020828403121561070c575f80fd5b81356001600160a01b0381168114610722575f80fd5b9392505050565b634e487b7160e01b5f52604160045260245ffd5b5f806040838503121561074e575f80fd5b82359150602083013567ffffffffffffffff8082111561076c575f80fd5b818501915085601f83011261077f575f80fd5b81358181111561079157610791610729565b604051601f8201601f19908116603f011681019083821181831017156107b9576107b9610729565b816040528281528860208487010111156107d1575f80fd5b826020860160208301375f6020848301015280955050505050509250929050565b838152606060208201525f61080a6060830185610638565b9050826040830152949350505050565b60208082526021908201527fc2a14c617320766f746163696f6e657320657374c3a16e2063657272616461736040820152602160f81b606082015260800190565b5f6001820161087857634e487b7160e01b5f52601160045260245ffd5b5060010190565b634e487b7160e01b5f52603260045260245ffd5b600181811c908216806108a757607f821691505b6020821081036108c557634e487b7160e01b5f52602260045260245ffd5b50919050565b60208082526034908201527fc2a1536f6c6f20656c2061646d696e6973747261646f72207075656465207265604082015273616c697a617220657374612061636369c3b36e2160601b606082015260800190565b601f82111561096657805f5260205f20601f840160051c810160208510156109445750805b601f840160051c820191505b81811015610963575f8155600101610950565b50505b505050565b815167ffffffffffffffff81111561098557610985610729565b610999816109938454610893565b8461091f565b602080601f8311600181146109cc575f84156109b55750858301515b5f19600386901b1c1916600185901b178555610a23565b5f85815260208120601f198616915b828110156109fa578886015182559484019460019091019084016109db565b5085821015610a1757878501515f19600388901b60f8161c191681555b505060018460011b0185555b50505050505056fea264697066735822122038c333978aac33bb2a514f1deb06173146b9a9aebb2951d58916a392cd5cb5b964736f6c63430008190033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDCANDIDATE = "addCandidate";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_CANDIDATES = "candidates";

    public static final String FUNC_CANDIDATESCOUNT = "candidatesCount";

    public static final String FUNC_CLOSEVOTING = "closeVoting";

    public static final String FUNC_GETCANDIDATES = "getCandidates";

    public static final String FUNC_HASVOTED = "hasVoted";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_VOTINGCLOSED = "votingClosed";

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addCandidate(BigInteger _id, String _name) {
        final Function function = new Function(
                FUNC_ADDCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_id), 
                new org.web3j.abi.datatypes.Utf8String(_name)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> admin() {
        final Function function = new Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, String, BigInteger>> candidates(BigInteger param0) {
        final Function function = new Function(FUNC_CANDIDATES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, String, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, String, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> candidatesCount() {
        final Function function = new Function(FUNC_CANDIDATESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> closeVoting() {
        final Function function = new Function(
                FUNC_CLOSEVOTING, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>> getCandidates() {
        final Function function = new Function(FUNC_GETCANDIDATES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>>(function,
                new Callable<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>>() {
                    @Override
                    public Tuple3<List<BigInteger>, List<String>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<BigInteger>, List<String>, List<BigInteger>>(
                                convertToNative((List<Uint256>) results.get(0).getValue()), 
                                convertToNative((List<Utf8String>) results.get(1).getValue()), 
                                convertToNative((List<Uint256>) results.get(2).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<Boolean> hasVoted(String param0) {
        final Function function = new Function(FUNC_HASVOTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _candidateId) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> votingClosed() {
        final Function function = new Function(FUNC_VOTINGCLOSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

        public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
            return new Voting(contractAddress, web3j, credentials, contractGasProvider);
        }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static class LinkReference {
        final String source;
        final String libraryName;
        final Address address;

        public LinkReference(String source, String libraryName, Address address) {
            this.source = source;
            this.libraryName = libraryName;
            this.address = address;
        }
    }

    public static String linkBinaryWithReferences(String binary, List<LinkReference> links) {
        String replacingBinary = binary;
        for (LinkReference link : links) {
            // solc / hardhat convention
            String libSourceName = link.source + ":" + link.libraryName;
            String placeHolder = "__$" + sha3String(libSourceName).substring(2, 36) + "$__";
            String addressReplacement = cleanHexPrefix(link.address.toString());
            replacingBinary = replacingBinary.replace(placeHolder, addressReplacement);

            // old version solc
            String linkString = link.source + ":" + link.libraryName;
            String oldSolcPlaceHolder =
                    "__" + linkString + "_".repeat(40 - linkString.length() - 2);
            replacingBinary = replacingBinary.replace(oldSolcPlaceHolder, addressReplacement);

            // truffle old version
            String trufflePlaceHolder =
                    "__" + link.libraryName + "_".repeat(40 - link.libraryName.length() - 2);
            replacingBinary = replacingBinary.replace(trufflePlaceHolder, addressReplacement);
        }
        return replacingBinary;
    }

    public static void linkLibraries(List<LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    public static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }
}
